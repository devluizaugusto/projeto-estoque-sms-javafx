package model.dao;

import db.DB;
import model.dao.impl.ProductDaoJDBC;
import model.dao.impl.SetoresDaoJDBC;

public class DaoFactory {

	public static ProductDao createProductDao() {
		return new ProductDaoJDBC(DB.getConnection());
	}
	
	public static SetoresDao createSetoresDao() {
		return new SetoresDaoJDBC(DB.getConnection());
	}
}
