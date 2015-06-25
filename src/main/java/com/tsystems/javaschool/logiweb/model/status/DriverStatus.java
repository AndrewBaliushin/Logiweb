package com.tsystems.javaschool.logiweb.model.status;

public enum DriverStatus {
    
    NOT_AVAILABLE(0),
    FREE(1),
    DRIVING(2),
    RESTING_EN_ROUT(3);
    
    private int idInDb;
    
    private DriverStatus(int idInDb) {
	this.idInDb = idInDb;
    }
    
    public static DriverStatus getById(int idInDb) {
	for(DriverStatus status : DriverStatus.values()) {
	    if(idInDb == status.getIdInDb()) {
		return status;
	    }
	}
	throw new IllegalArgumentException("No mathcing DriverStatus for id: "
	        + idInDb);
    }
    
    public int getIdInDb() {
        return idInDb;
    }

    @Override
    public String toString() {
	String name = name();
	return name.replaceAll("_", " ");	//replace underscores with spaces
    }

}
