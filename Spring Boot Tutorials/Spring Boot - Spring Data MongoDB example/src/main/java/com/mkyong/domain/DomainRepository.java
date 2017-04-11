package com.mkyong.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DomainRepository extends MongoRepository<Domain, Long>, DomainRepositoryCustom {

    Domain findFirstByDomain(String domain);

    Domain findByDomainAndDisplayAds(String domain, boolean displayAds);

    //Mongo JSON query string
    @Query("{domain:'?0'}")
    Domain findCustomByDomain(String domain);

    @Query("{domain: { $regex: ?0 } })")
    List<Domain> findCustomByRegExDomain(String domain);

}
