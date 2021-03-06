/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desafio.api.service.impl;

import com.desafio.api.domain.Medico;
import com.desafio.api.repository.MedicoRepository;
import com.desafio.api.service.MedicoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author deoprog
 */
@Service
public class MedicoServiceImpl implements MedicoService {
    
    private static final Logger log = LoggerFactory.getLogger(Medico.class);
    
    @Autowired
    private MedicoRepository medicoRepository;
    
    @Override
    public Optional<Medico> findByNome(String nome) {
        log.info("Buscando nome do medico {}", nome);
        return Optional.ofNullable(this.medicoRepository.findByNome(nome));
    }
    
    @Override
    @Cacheable("medicoPorId")
    public Optional<Medico> findById(Long id) {
        return Optional.ofNullable(this.medicoRepository.findOne(id));
    }

    @Override    
    public Medico save(Medico entity) {
        log.info("Persistir medico: {}", entity);
        return this.medicoRepository.save(entity);
    }

    @Override
    @CachePut("medicoPorId")
    public void update(Medico entity) {
        this.medicoRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        this.medicoRepository.delete(id);
    }       

    @Override
    public List<Medico> findAll() {
        return this.medicoRepository.findAll();
    }

    @Override
    public Medico find(Long id) {
        return this.medicoRepository.findOne(id);
    }
    
}
