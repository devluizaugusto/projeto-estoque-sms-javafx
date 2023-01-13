package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SetoresDao;
import model.entities.Setores;

public class SetoresService {

private SetoresDao dao = DaoFactory.createSetoresDao();
	
	public List<Setores> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Setores obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Setores obj) {
		dao.deleteById(obj.getId());
	}
}
