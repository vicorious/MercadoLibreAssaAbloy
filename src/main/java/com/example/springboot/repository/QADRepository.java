package com.example.springboot.repository;

import com.example.springboot.dto.CustomResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

import com.example.springboot.entity.*;

@Transactional
@Repository
public interface QADRepository extends CrudRepository<Ld_det, Long> {

    /**
     * Esto es el script
     */
    @Query("select new com.example.springboot.dto.CustomResponse (p.pt_domain, p.pt_part, p.pt_article,"+
            "    p.pt_desc1,"+
            "    p.pt_desc2,"+
            "    p.pt_status,"+
            "    e.ld_domain,"+
            "    e.ld_site,"+
            "    e.ld_loc,"+
            "    e.ld_qty_oh) from Ld_det e, Pt_mstr p "+
            "    WHERE e.ld_domain = 'cerracol' AND"+
            "    e.ld_site        = 'alm_destino'  AND"+
            "    e.ld_loc         = 'TRA001' AND "+
            "    e.ld_qty_oh > 0"+
            "    AND p.pt_domain  = 'cerracol'  AND"+
            "        p.pt_part        = e.ld_part")
    List<CustomResponse> getInfoFromQAD();
}