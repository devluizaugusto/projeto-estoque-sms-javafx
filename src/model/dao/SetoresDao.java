package model.dao;

import java.util.List;

import model.entities.Setores;

public interface SetoresDao {
	void insert(Setores obj);
	void update(Setores obj);
	void deleteById(Integer id);
	Setores findById(Integer id);
	List<Setores> findAll();
}
