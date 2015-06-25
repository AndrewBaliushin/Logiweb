package com.tsystems.javaschool.logiweb.model.status;

/**
 * Enum representation of statuses for cargo.
 * 
 * @author Andrey Baliushin
 */
public enum CargoStatus {

    WAITING_FOR_PICKUP(0),
    PICKED_UP(1),
    DELIVERED(2);
    
    private int idInDb;
    
    private CargoStatus(int idInDb) {
	this.idInDb = idInDb;
    }
    
    public static CargoStatus getById(int idInDb) {
	for(CargoStatus status : CargoStatus.values()) {
	    if(idInDb == status.getIdInDb()) {
		return status;
	    }
	}
	throw new IllegalArgumentException("No mathcing CargoStatus for id: "
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
