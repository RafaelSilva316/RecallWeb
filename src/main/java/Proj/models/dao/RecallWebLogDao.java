package Proj.models.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Proj.models.RecallWebLog;

@Transactional
@Repository
public interface RecallWebLogDao extends CrudRepository<RecallWebLog, Integer>{

	public List<RecallWebLog> findAll();
	
	public RecallWebLog findByUid(Integer uid);
	
	public List<RecallWebLog> findByName(String name);
}
