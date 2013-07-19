package com.polarisind.services.dealerservice.dataaccess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polarisind.schemas.dealerservice.DealerType;

public class DealerUtilityImpl implements DealerUtility{

	public static final DealerUtilityImpl INSTANCE = new DealerUtilityImpl();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DealerUtilityImpl.class);

	public static final String QUERY = "{call Orders.GetDealerOrderHeader (?)}";
	
	private DealerUtilityImpl() { }
	
	@Override
	public DealerType getDealer(String dealerID, DataSource dataSource) {
		DealerType result = null;
		try {
			result = getDealerType(dealerID, dataSource);
		} catch (SQLException e) {
			LOGGER.error("Failed to create DealerType.", e);
		}
		
		return result;
	}
	
	private DealerType getDealerType(String dealerID, DataSource dataSource) throws SQLException {
		DealerType result = null;
		
		ResultSet data = null;
		Connection conn = null;
		CallableStatement stmt = null;
		
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareCall(QUERY);
			data = loadDealerData(dealerID, stmt);
			result = DealerTypeBuilder.INSTANCE.build(data);
		} finally {
			DbUtils.closeQuietly(data);
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(conn);
		}
		
		return result;
	}
	
	private ResultSet loadDealerData(String dealerID, CallableStatement stmt) throws SQLException {
		ResultSet result = null;

		stmt.setString("@DealerId", dealerID);
		
		result = stmt.executeQuery();

		return result;
	}

}
