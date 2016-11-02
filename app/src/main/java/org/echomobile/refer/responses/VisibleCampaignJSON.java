package org.echomobile.refer.responses;


import org.echomobile.refer.objects.Campaign;

import java.util.List;

public class VisibleCampaignJSON extends BaseJSONResponse {
    public Campaign[] campaigns;

    public class Data {
        public List<RecordJSON> records;

    }

    VisibleCampaignJSON() {
	}
}

