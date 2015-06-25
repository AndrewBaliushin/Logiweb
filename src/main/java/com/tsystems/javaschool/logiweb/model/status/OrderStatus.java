package com.tsystems.javaschool.logiweb.model.status;

public enum OrderStatus {

    NOT_READY(0),
    READY_TO_GO(1),
    DELIVERED(2);
    
    private int idInDb;
    
    private OrderStatus(int idInDb) {
	this.idInDb = idInDb;
    }
    
    public static OrderStatus getById(int idInDb) {
	for(OrderStatus status : OrderStatus.values()) {
	    if(idInDb == status.getIdInDb()) {
		return status;
	    }
	}
	throw new IllegalArgumentException("No mathcing OrderStatus for id: "
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
