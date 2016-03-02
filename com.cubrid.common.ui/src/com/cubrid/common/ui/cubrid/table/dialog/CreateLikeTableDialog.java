/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search
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
package com.cubrid.common.ui.cubrid.table.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cubrid.common.ui.cubrid.table.Messages;
import com.cubrid.common.ui.spi.dialog.CMTitleAreaDialog;
import com.cubrid.common.ui.spi.model.CubridDatabase;
import com.cubrid.common.ui.spi.progress.CommonTaskExec;
import com.cubrid.common.ui.spi.progress.ExecTaskWithProgress;
import com.cubrid.common.ui.spi.progress.TaskExecutor;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.common.ui.spi.util.ValidateUtil;
import com.cubrid.cubridmanager.core.cubrid.table.task.CreateLikeTableTask;

/**
 * 
 * In MySQL compatible mode,create table by like statement dialog
 * 
 * @author pangqiren
 * @version 1.0 - 2010-4-19 created by pangqiren
 */
public class CreateLikeTableDialog extends
		CMTitleAreaDialog implements
		ModifyListener {
	private Text likeTableNameText = null;
	private Text newTableNameText = null;
	private CubridDatabase database = null;
	private String likeTableName = null;
	private String newTableName = null;

	/**
	 * The constructor
	 * 
	 * @param parentShell
	 */
	public CreateLikeTableDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create dialog area content
	 * 
	 * @param parent the parent composite
	 * @return the control
	 */
	protected Control createDialogArea(Composite parent) {
		Composite parentComp = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(parentComp, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);

		Label tableNameLabel = new Label(composite, SWT.LEFT);
		tableNameLabel.setText(Messages.lblLikeTableName);
		tableNameLabel.setLayoutData(CommonUITool.createGridData(1, 1, -1, -1));
		likeTableNameText = new Text(composite, SWT.LEFT | SWT.BORDER);
		likeTableNameText.setLayoutData(CommonUITool.createGridData(
				GridData.FILL_HORIZONTAL, 2, 1, 100, -1));

		Label newTableNameLabel = new Label(composite, SWT.LEFT);
		newTableNameLabel.setText(Messages.lblNewTableName);
		newTableNameLabel.setLayoutData(CommonUITool.createGridData(1, 1, -1, -1));
		newTableNameText = new Text(composite, SWT.LEFT | SWT.BORDER);
		newTableNameText.setTextLimit(ValidateUtil.MAX_SCHEMA_NAME_LENGTH);
		newTableNameText.setLayoutData(CommonUITool.createGridData(
				GridData.FILL_HORIZONTAL, 2, 1, 100, -1));
		newTableNameText.addModifyListener(this);

		if (likeTableName == null) {
			likeTableNameText.addModifyListener(this);
			likeTableNameText.setFocus();
		} else {
			likeTableNameText.setText(likeTableName);
			likeTableNameText.setEnabled(false);
			newTableNameText.setFocus();
		}

		setTitle(Messages.titleCreateLikeTableDialog);
		setMessage(Messages.msgCreateLikeTableDialog);
		return parentComp;
	}

	/**
	 * Constrain the shell size
	 */
	protected void constrainShellSize() {
		super.constrainShellSize();
		getShell().setSize(450, 240);
		CommonUITool.centerShell(getShell());
		getShell().setText(Messages.titleCreateLikeTableDialog);
	}

	/**
	 * Create buttons for button bar
	 * 
	 * @param parent the parent composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				com.cubrid.common.ui.common.Messages.btnOK, true);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				com.cubrid.common.ui.common.Messages.btnCancel, false);
	}

	/**
	 * When press button,call it
	 * 
	 * @param buttonId the button id
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			createTable();
		} else {
			super.buttonPressed(buttonId);
		}
	}

	/**
	 * 
	 * Execute task to create table
	 * 
	 */
	private void createTable() {
		newTableName = newTableNameText.getText();
		String taskName = Messages.bind(Messages.createLikeTableTaskName,
				newTableName);
		TaskExecutor executor = new CommonTaskExec(taskName);
		CreateLikeTableTask task = new CreateLikeTableTask(
				getDatabase().getDatabaseInfo());
		likeTableName = likeTableNameText.getText();
		task.setTableName(newTableName);
		task.setLikeTableName(likeTableName);
		executor.addTask(task);
		new ExecTaskWithProgress(executor).exec();
		if (task.isSuccess()) {
			super.buttonPressed(IDialogConstants.OK_ID);
		}
	}

	/**
	 * Listen to the modify event
	 * 
	 * @param event the modify event
	 */
	public void modifyText(ModifyEvent event) {
		String newTableName = newTableNameText.getText();
		String likeTableName = likeTableNameText.getText();
		boolean isValid = true;
		if ("".equals(newTableName) || "".equals(likeTableName)) {
			setErrorMessage(Messages.errNoTableName);
			isValid = false;
		} else {
			if (!ValidateUtil.isValidIdentifier(newTableName)) {
				setErrorMessage(Messages.bind(
						Messages.renameInvalidTableNameMSG, "table",
						newTableName));
				isValid = false;
				newTableNameText.selectAll();
				newTableNameText.setFocus();
			} else if (newTableName.equalsIgnoreCase(likeTableName)) {
				setErrorMessage(Messages.bind(Messages.errExistTable,
						newTableName));
				isValid = false;
				newTableNameText.setFocus();
			} else if (!ValidateUtil.isASCII(newTableName)
					&& !ValidateUtil.isSupportMultiByte(database)) {
				setErrorMessage(Messages.errMultiBytes);
				isValid = false;
				newTableNameText.selectAll();
				newTableNameText.setFocus();
			}
		}
		if (isValid) {
			setErrorMessage(null);
		}
		getButton(IDialogConstants.OK_ID).setEnabled(isValid);
	}

	/**
	 * 
	 * Get CUBRID Database
	 * 
	 * @return the CubridDatabase object
	 */
	public CubridDatabase getDatabase() {
		return database;
	}

	/**
	 * 
	 * Set CUBRID Database
	 * 
	 * @param database the CubridDatabase object
	 */
	public void setDatabase(CubridDatabase database) {
		this.database = database;
	}

	public String getLikeTableName() {
		return likeTableName;
	}

	public void setLikeTableName(String likeTableName) {
		this.likeTableName = likeTableName;
	}

	/**
	 * 
	 * Return the new table name
	 * 
	 * @return The string
	 */
	public String getNewTableName() {
		return newTableName;
	}
}
