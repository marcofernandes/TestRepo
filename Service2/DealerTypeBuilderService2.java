package com.polarisind.services.dealerservice.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polarisind.schemas.dealerservice.AddressType;
import com.polarisind.schemas.dealerservice.DealerType;

public class DealerTypeBuilder {

	public static final DealerTypeBuilder INSTANCE = new DealerTypeBuilder();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DealerTypeBuilder.class);
	
	private DealerTypeBuilder() { }
	
	public DealerType build(ResultSet data) throws SQLException {
		DealerType result = new DealerType();
		
		if(data != null && data.next()) {
			result.setDSM(getResultString(data, "DSMName"));
			result.setDSMEmailAddress(getResultString(data, "DSMEmail"));
			//Legacy session object doesn't have leading 0's, need to remove them
			result.setDealerID(getResultString(data, "DealerID").replaceFirst("^0+(?!$)", ""));
			result.setName(getResultString(data, "DealerName"));
			result.setPhone(getResultString(data, "Phone"));
			result.setDealerEmail(getResultString(data, "DealerEmail"));
			
			AddressType address= new AddressType();
			address.setCity(getResultString(data, "City"));
			address.setPostalCode(getResultString(data, "Zip"));
			address.setState(getResultString(data, "State"));
			address.setStreet(getResultString(data, "Street"));
			result.setAddress(address);
		} else {
			LOGGER.warn("DealerType creation has failed due to missing data.");
		}
		
		return result;
	}
	

	private String getResultString(ResultSet data, String string) {
		String result = " ";
		try{			
			result = StringUtils.defaultIfBlank(data.getString(string), " ");
		} catch(SQLException e){
			LOGGER.error("Failed to load result field: " + string, e);
		}
		return result;
	}
}
