package com.cubrid.common.ui.cubrid.database.erwin.xmlmodel;

import javax.xml.bind.annotation.XmlElement;

import com.cubrid.common.ui.cubrid.database.erwin.xmlmodel.DefaultPropsList.ServerValue;

public class DefaultValueProps {

	@XmlElement(name = "Server_Value")
	protected ServerValue serverValue;

	public ServerValue getServerValue() {
		return serverValue;
	}

	public void setServerValue(ServerValue serverValue) {
		this.serverValue = serverValue;
	}

}
