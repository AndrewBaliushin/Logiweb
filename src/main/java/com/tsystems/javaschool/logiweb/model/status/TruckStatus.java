package com.tsystems.javaschool.logiweb.model.status;

public enum TruckStatus {
    
    FAULTY(0),
    OK(1);
    
    private int idInDb;
    
    private TruckStatus(int idInDb) {
	this.idInDb = idInDb;
    }
    
    public static TruckStatus getById(int idInDb) {
	for(TruckStatus status : TruckStatus.values()) {
	    if(idInDb == status.getIdInDb()) {
		return status;
	    }
	}
	throw new IllegalArgumentException("No mathcing TruckStatus for id: "
	        + idInDb);
    }
    
    public int getIdInDb() {
        return idInDb;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
