package com.polarisind.services.dealerservice.dataaccess;

import javax.sql.DataSource;

import com.polarisind.schemas.dealerservice.DealerType;

public interface DealerUtility {

	public DealerType getDealer(String dealerID, DataSource dataSource);
}
