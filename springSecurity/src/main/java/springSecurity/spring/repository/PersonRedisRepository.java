package springSecurity.spring.repository;

import org.springframework.data.repository.CrudRepository;
import springSecurity.spring.entity.Person;

public interface PersonRedisRepository extends CrudRepository<Person,String> {
}
