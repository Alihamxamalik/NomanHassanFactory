package model;

public class Production {
    private long id,workerId,itemId;
    private String date;
    private double weight;

    public Production (long itemId,double weight,String date){
     this.itemId =itemId;
     this.weight = weight;
     this.date = date;
    }
    public Production (long id,long itemId,double weight,String date,long workerId){
        this.itemId =itemId;
        this.weight = weight;
        this.date = date;
        this.id = id;
        this.workerId = workerId;
    }
    public Production (long id, long itemId, double weight, String date){
        this.itemId =itemId;
        this.weight = weight;
        this.date = date;
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getItemId() {
        return itemId;
    }

    public String getDate() {
        return date;
    }

    public double getWeight() {
        return weight;
    }



}
