package com.tharushinis.Ocean_view_resort.model;


import java.sql.Timestamp;

public abstract class BaseModel{
    protected int id;
    protected Timestamp createdAt;
    protected Timestamp updateAt;

   public BaseModel(){
       this.createdAt = new Timestamp(System.currentTimeMillis());
       this.updateAt = new Timestamp(System.currentTimeMillis());
   }

  public abstract String getDisplayName();
   //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{id=" + id + "}";
    }
}
