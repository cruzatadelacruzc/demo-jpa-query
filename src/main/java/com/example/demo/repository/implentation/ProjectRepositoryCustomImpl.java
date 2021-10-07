package com.example.demo.repository.implentation;

import com.example.demo.domain.Project;
import com.example.demo.repository.ProjectRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Project> findOneWithUsersByCentro(String centro) {
        /** TODO No se como es que se hace una consulta cargando las relaciones(users) de la entidad. Te toca hacerla
         * FIXME Para que funcione debes ir a {@link Project} comentarear o remover la anotacion @JsonIgnore en el atributo users
         */
        return Optional.empty();
    }

    @Override
    public Optional<Project> findOneByCentro(String centro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> projectRoot = query.from(Project.class);
        Path<String> centroPath = projectRoot.get("centro");
        query.select(projectRoot).where(cb.equal(centroPath, centro));
        return Optional.of(entityManager.createQuery(query).getSingleResult());
    }

    @Override
    public List<Project> findAllByCentros(List<String> centros) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> projectRoot = query.from(Project.class);
        Path<String> centroPath = projectRoot.get("centro");
        List<Predicate> predicates = new ArrayList<>();
        centros.forEach(centro -> predicates.add(cb.equal(centroPath, centro)));
        query.select(projectRoot).where(cb.or(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
