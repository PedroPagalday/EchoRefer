package org.echomobile.refer.responses;


import org.echomobile.refer.objects.Client;

public class AuthenticateUserResponseJSON extends BaseJSONResponse {
    public Data data;

	public class Data {
		public Client client;
	}

	AuthenticateUserResponseJSON() {
	}
}
