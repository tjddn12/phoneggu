package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
