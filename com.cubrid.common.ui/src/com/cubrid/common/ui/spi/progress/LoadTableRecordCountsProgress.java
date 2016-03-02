/*
 * Copyright (C) 2013 Search Solution Corporation. All rights reserved by Search
 * Solution.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: -
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. - Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. - Neither the name of the <ORGANIZATION> nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.cubrid.common.ui.spi.progress;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;

import com.cubrid.common.core.common.model.TableDetailInfo;
import com.cubrid.common.core.util.LogUtil;
import com.cubrid.common.core.util.QuerySyntax;
import com.cubrid.common.core.util.QueryUtil;
import com.cubrid.common.ui.query.control.QueryExecuter;
import com.cubrid.common.ui.spi.Messages;
import com.cubrid.common.ui.spi.model.CubridDatabase;
import com.cubrid.cubridmanager.core.common.jdbc.JDBCConnectionManager;
import com.cubrid.jdbc.proxy.driver.CUBRIDPreparedStatementProxy;

public class LoadTableRecordCountsProgress implements IRunnableWithProgress {
	private static final Logger LOGGER = LogUtil.getLogger(LoadTableRecordCountsProgress.class);
	private final CubridDatabase database;
	private final List<TableDetailInfo> tableList;
	private boolean success = false;

	public LoadTableRecordCountsProgress(CubridDatabase database, List<TableDetailInfo> tableList) {
		this.database = database;
		this.tableList = tableList;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		Connection conn = null;
		monitor.beginTask(Messages.loadTableRecordCountsProgressTaskName,
				tableList.size());
		try {
			conn = JDBCConnectionManager.getConnection(
					database.getDatabaseInfo(), true);
			
			for (TableDetailInfo tablesDetailInfo : tableList) {
				monitor.subTask(Messages.bind(
						Messages.loadTableRecordCountsProgressSubTaskName,
						tablesDetailInfo.getTableName()));
				
				int recordCount = getRecordsCount(conn,
						tablesDetailInfo.getTableName());
				tablesDetailInfo.setRecordsCount(recordCount);
				
				monitor.worked(1);
				if (monitor.isCanceled()) {
					break;
				}
			}
			success = true;
		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			QueryUtil.freeQuery(conn);
			monitor.done();
		}
	}

	protected int getRecordsCount(Connection conn, String tableName) {
		int recordsCount = 0;
		try {
			if (conn == null || conn.isClosed()) {
				return recordsCount;
			}
		} catch (SQLException e) {
			LOGGER.error("", e);
		}

		String sql = "SELECT COUNT(*) FROM " + QuerySyntax.escapeKeyword(tableName);

		// [TOOLS-2425]Support shard broker
		if (CubridDatabase.hasValidDatabaseInfo(database)) {
			sql = database.getDatabaseInfo().wrapShardQuery(sql);
		}

		CUBRIDPreparedStatementProxy stmt = null;
		ResultSet rs = null;
		try {
			stmt = QueryExecuter.getStatement(conn, sql, false, false);
			rs = stmt.executeQuery();
			if (rs.next()) {
				recordsCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.error("", e);
			e.printStackTrace();
		} finally {
			QueryUtil.freeQuery(stmt, rs);
		}

		return recordsCount;
	}
	
	/**
	 * loadTablesInfo
	 * 
	 * @return Catalog
	 */
	public void getTableCounts() {
		Display display = Display.getDefault();
		display.syncExec(new Runnable() {
			public void run() {
				try {
					new ProgressMonitorDialog(null).run(true, true,
							LoadTableRecordCountsProgress.this);
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		});
	}
	
	public boolean isSuccess() {
		return success;
	}
}
