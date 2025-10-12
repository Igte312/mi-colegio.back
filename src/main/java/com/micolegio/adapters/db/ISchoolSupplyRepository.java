package com.micolegio.adapters.db;

import com.micolegio.domain.SchoolSupply;

import java.util.List;

public interface ISchoolSupplyRepository {

    List<SchoolSupply> getGeneralActiveSupplies();
}