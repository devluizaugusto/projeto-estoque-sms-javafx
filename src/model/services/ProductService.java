package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Product;

public class ProductService {

	public List<Product> findAll(){
		List<Product> list = new ArrayList<>();
		list.add(new Product(1, "Teclado USB", 100, 1, 99));
		list.add(new Product(1, "Mouse USB", 100, 1, 99));
		
		return list;
	}
}
