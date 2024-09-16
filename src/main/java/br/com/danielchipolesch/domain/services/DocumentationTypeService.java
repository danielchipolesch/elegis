package br.com.danielchipolesch.domain.services;

import br.com.danielchipolesch.domain.exceptions.DocumentationTypeException;
import br.com.danielchipolesch.domain.dtos.documentationTypeDtos.DocumentationTypeRequestCreateDto;
import br.com.danielchipolesch.domain.dtos.documentationTypeDtos.DocumentationTypeResponseDto;
import br.com.danielchipolesch.domain.dtos.documentationTypeDtos.DocumentationTypeRequestUpdateDto;
import br.com.danielchipolesch.domain.entities.documentationNumbering.DocumentationType;
import br.com.danielchipolesch.infrastructure.repositories.DocumentationTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentationTypeService {

    @Autowired
    private DocumentationTypeRepository documentationTypeRepository;

    @Autowired
    private ModelMapper modelMapper;

    public DocumentationTypeResponseDto create(DocumentationTypeRequestCreateDto request) throws Exception {

        if(documentationTypeRepository.existsByAcronym(request.getAcronym())){
            throw new Exception(DocumentationTypeException.ALREADY_EXISTS.getMessage());
        }

        DocumentationType documentationType = modelMapper.map(request, DocumentationType.class);
        documentationTypeRepository.save(documentationType);
        return modelMapper.map(documentationType, DocumentationTypeResponseDto.class);
    }


    public DocumentationTypeResponseDto update(Long id, DocumentationTypeRequestUpdateDto request) throws Exception{

        DocumentationType documentationType = documentationTypeRepository.findById(id)
                .orElseThrow(() -> new Exception(DocumentationTypeException.NOT_FOUND.getMessage()));

        documentationType.setAcronym(request.getAcronym().isBlank() ? documentationType.getAcronym() : request.getAcronym());
        documentationType.setName(request.getName().isBlank() ? documentationType.getName() : request.getName());
        documentationType.setDescription(request.getDescription().isBlank() ? documentationType.getDescription() : request.getDescription());

        documentationTypeRepository.save(documentationType);

        return modelMapper.map(documentationType, DocumentationTypeResponseDto.class);
    }

    public DocumentationTypeResponseDto delete(Long id) throws Exception {

        DocumentationType documentationType =
                documentationTypeRepository.findById(id).orElseThrow(() -> new Exception(DocumentationTypeException.NOT_FOUND.getMessage()));

        documentationTypeRepository.delete(documentationType);

        return modelMapper.map(documentationType, DocumentationTypeResponseDto.class);
    }

    public DocumentationTypeResponseDto getById(Long id) throws Exception {

        DocumentationType documentationType =
                documentationTypeRepository.findById(id).orElseThrow(() -> new Exception(DocumentationTypeException.NOT_FOUND.getMessage()));

        return modelMapper.map(documentationType, DocumentationTypeResponseDto.class);
    }

    public List<DocumentationTypeResponseDto> getAll(Pageable pageable) throws Exception {
        Page<DocumentationType> documentationTypes = documentationTypeRepository.findAll(pageable);

        List<DocumentationTypeResponseDto> responseDtos = documentationTypes.stream().map(documentationType -> modelMapper.map(documentationType, DocumentationTypeResponseDto.class)).toList();

        return responseDtos;
    }
}
