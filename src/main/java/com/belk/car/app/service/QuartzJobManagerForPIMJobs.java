package com.belk.car.app.service;

import java.util.List;

import com.belk.car.app.model.car.ManualCar;
import com.belk.car.integrations.pim.xml.DropShipMessage;
import com.belk.car.integrations.pim.xml.GroupingMessage;
import com.belk.car.integrations.pim.xml.PoMessage;
import com.belk.car.integrations.pim.xml.RLRItems;


public interface QuartzJobManagerForPIMJobs 
{
        public void processGrouping(GroupingMessage groupingMessage) throws Exception;
	public void processPOCAR(PoMessage poMessage) throws Exception;
	public ManualCar processManualCAR(ManualCar manualcar)throws Exception ;
	public void processDropShipCAR(DropShipMessage dropshipMessage) throws Exception;
	public void processRLRMessage(RLRItems rlrMessage) throws Exception;
	public void processPIMAttributeUpdates() throws Exception;
    public void executeSkuPackParentResyncProcedure();
    public void processPIMAttributeUpdatesForClosedCars()throws Exception;
    public void saveGroupingFailureInDB(List<String> styleOrins,String groupingMessage);
}