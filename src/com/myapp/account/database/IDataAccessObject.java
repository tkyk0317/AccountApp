package com.myapp.account.database;

import java.util.*;

//==================================.
// DataAccessObjectInterface.
public interface IDataAccessObject <EntityType> {

    // Get Record specified EntityKey.
    public EntityType get(int key);

    // Get All Record.
    public List<EntityType> getAll();

    // Add Record in Entity.
    public int insert(EntityType record);

    // Delete Record from Entity.
    public boolean delete(int key);

    // Update Record.
    public boolean update(EntityType record);
}
