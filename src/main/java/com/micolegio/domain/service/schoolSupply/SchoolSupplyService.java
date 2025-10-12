package com.micolegio.domain.service.schoolSupply;

import com.micolegio.adapters.db.ISchoolSupplyRepository;
import com.micolegio.domain.SchoolSupply;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SchoolSupplyService implements ISchoolSupplyService{

    private final ISchoolSupplyRepository schoolSupplyRepository;

    @Override
    public List<SchoolSupply> getGeneralActiveSupplies() {
        return schoolSupplyRepository.getGeneralActiveSupplies();
    }
}
