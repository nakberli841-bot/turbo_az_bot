package com.nurlan.turboazbot.turbo_az_bot.repo;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarAddCustomRepo {

    private final EntityManager em;

    public CarAddCustomRepo(EntityManager em) {
        this.em = em;
    }

    public List<CarAd> searchCarAds(  //criteria api nin mentiqini daha detalli oyren
            String title,
            String year,
            String price,
            String createdAt,
            String link,
            String externalId,
            int page,
            int size
            //String sort
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CarAd> cq = cb.createQuery(CarAd.class);
        Root<CarAd> root = cq.from(CarAd.class);

        List<Predicate> predicates = new ArrayList<>();

        // Dinamik like/equal şərtləri
        if (title != null && !title.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (year != null && !year.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("year")), "%" + year.toLowerCase() + "%"));
        }

        if (price != null && !price.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("price")), "%" + price.toLowerCase() + "%"));
        }

        if (createdAt != null && !createdAt.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("createdAt")), "%" + createdAt.toLowerCase() + "%"));
        }

        if (link != null && !link.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("link")), "%" + link.toLowerCase() + "%"));
        }

        if (externalId != null && !externalId.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("externalId")), "%" + externalId.toLowerCase() + "%"));
        }

        // Bütün predicate-ləri AND ilə birləşdir
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

//        String[] sortParams = sort.split(",");
//        String sortField = sortParams[0];
//        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
//
//        if (direction.isAscending()) {
//            cq.orderBy(cb.asc(root.get(sortField)));
//        } else {
//            cq.orderBy(cb.desc(root.get(sortField)));
//        }

        // --- Query yarat ---
        TypedQuery<CarAd> query = em.createQuery(cq);

        // --- Pagination (səhifələmə) ---
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        // --- Nəticə ---
        return query.getResultList();
    }

}
