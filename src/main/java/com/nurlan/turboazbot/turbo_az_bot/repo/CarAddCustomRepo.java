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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarAddCustomRepo {

    private final EntityManager em;

    public CarAddCustomRepo(EntityManager em) {
        this.em = em;
    }

    public List<CarAd> searchCarAds(// bu metodun isleme prinsipini detalli oyren
            String title,
            Integer yearFrom, Integer yearTo,
            Integer priceFrom, Integer priceTo,
            LocalTime createdAtFrom, LocalTime createdAtTo
    ) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CarAd> cq = cb.createQuery(CarAd.class);
        Root<CarAd> root = cq.from(CarAd.class);

        List<Predicate> predicates = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (yearFrom != null && yearTo != null) {
            predicates.add(cb.between(root.get("year"), yearFrom, yearTo));
        } else if (yearFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("year"), yearFrom));
        } else if (yearTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("year"), yearTo));
        }

        if (priceFrom != null && priceTo != null) {
            predicates.add(cb.between(root.get("price"), priceFrom, priceTo));
        } else if (priceFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), priceFrom));
        } else if (priceTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), priceTo));
        }

        if (createdAtFrom != null && createdAtTo != null) {
            predicates.add(cb.between(root.get("createdAt"), createdAtFrom, createdAtTo));
        } else if (createdAtFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), createdAtFrom));
        } else if (createdAtTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdAtTo));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));


        cq.orderBy(cb.desc(root.get("createdAt")));


        TypedQuery<CarAd> query = em.createQuery(cq);
        return query.getResultList();
    }

}