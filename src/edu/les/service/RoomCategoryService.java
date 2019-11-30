package edu.les.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import edu.les.entity.RoomCategoryEntity;
import edu.les.exception.ExceptionHandler;
import edu.les.repository.RoomCategoryRepository;

@Service
public class RoomCategoryService {
	@Autowired
	private RoomCategoryRepository roomCategoryRepository;
	
	public Iterable<RoomCategoryEntity> fetchAll() {
		return this.roomCategoryRepository.findAll();
	}
	
	public void addOrUpdate(RoomCategoryEntity roomCategoryEntity) throws ExceptionHandler {
		if (!this.hasErrors(roomCategoryEntity)) {
			try {
				this.roomCategoryRepository.save(roomCategoryEntity);
			} catch (JpaSystemException e) {
				throw new ExceptionHandler("A category with name: \"" + roomCategoryEntity.getCategory() + "\" already exists.");
			}
		}
	}
	
	public RoomCategoryEntity findById(int id) throws ExceptionHandler {
		Optional<RoomCategoryEntity> r = this.roomCategoryRepository.findById(id);
		if (!r.isPresent()) {
			throw new ExceptionHandler("Category not found!");
		}
		return r.get();
	}

	public RoomCategoryEntity fetchByCategory(String category) throws ExceptionHandler {
		Optional<RoomCategoryEntity> r = this.roomCategoryRepository.fetchByCategory(category);
		if (!r.isPresent()) {
			throw new ExceptionHandler("Category not found!");
		}
		return r.get();
	}
	
	public boolean hasErrors(RoomCategoryEntity c) throws ExceptionHandler {
		boolean result = true;
		List<String> fieldsWithError = new ArrayList<String>();
		if (c.getCategory().length() == 0 || c.getCategory().length() > 25) {
			fieldsWithError.add("Category");
		}
		
		// TODO: check if need any value validation
		
		if (c.getMaxPeople() > 9) {
			fieldsWithError.add("Number of People");
		}
		
		if (fieldsWithError.isEmpty()) {
			result = false;
		} else {
			throw new ExceptionHandler(fieldsWithError);
		}
		
		return result;
	}

	public void deleteById(int id) throws ExceptionHandler {
		this.roomCategoryRepository.deleteById(id);
	}
}
