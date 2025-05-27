package com.meditourism.meditourism.contactForm.service;

import com.meditourism.meditourism.blockedUser.service.BlockedUserService;
import com.meditourism.meditourism.blockedUser.service.IBlockedUserService;
import com.meditourism.meditourism.contactForm.dto.ContactFormRequestDTO;
import com.meditourism.meditourism.contactForm.dto.ContactFormResponseDTO;
import com.meditourism.meditourism.contactForm.dto.TreatmentContactCountDTO;
import com.meditourism.meditourism.contactForm.entity.ContactFormEntity;
import com.meditourism.meditourism.contactForm.repository.ContactFormRepository;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.exception.UnauthorizedAccessException;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactFormService implements IContactFormService {


    @Autowired
    private ContactFormRepository contactFormRepository;
    @Autowired
    private IBlockedUserService blockedUserService;

    /**
     * @return 
     */
    @Override
    public List<ContactFormResponseDTO> getAllContactForms() {
        return ContactFormResponseDTO.fromEntityList(contactFormRepository.findAll());
    }

    @Override
    public List<ContactFormResponseDTO> getAllContactFormsByTreatmentId(Long id) {
        List<ContactFormEntity> contactForms = contactFormRepository.findAllByTreatmentId(id);

        if (contactForms.isEmpty()) {
            throw new ResourceNotFoundException("No hay formularios de contacto relacionados al tratamiento con ID: " + id);
        }

        return ContactFormResponseDTO.fromEntityList(contactForms);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<ContactFormResponseDTO> getContactFormByUserId(Long id) {
        List<ContactFormEntity> contactForms = contactFormRepository.findAllByUserId(id);

        if (contactForms.isEmpty()) {
            throw new ResourceNotFoundException("No hay formularios relacionados al usuario con ID: " + id);
        }
        return ContactFormResponseDTO.fromEntityList(contactForms);
    }


    /**
     * @param id 
     * @return
     */
    @Override
    public ContactFormResponseDTO getContactFormById(Long id) {
        return new ContactFormResponseDTO(contactFormRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el formulaio de contacto con ID: "+ id)));
    }

    public List<TreatmentContactCountDTO> getTreatmentContactCounts() {
        List<Object[]> results = contactFormRepository.findTreatmentContactCounts();
        return results.stream()
                .map(obj -> new TreatmentContactCountDTO(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        ((Number) obj[2]).longValue()))
                .toList();
    }


    /**
     * @param dto
     * @return
     */
    @Override
    public ContactFormResponseDTO saveContactForm(ContactFormRequestDTO dto) {
        if (blockedUserService.isBlocked(dto.getUserId())) {
            throw new UnauthorizedAccessException("No tienes permisos");
        }
        ContactFormEntity entity = new ContactFormEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(dto.getUserId());
        TreatmentEntity treatmentEntity = new TreatmentEntity();
        treatmentEntity.setId(dto.getTreatmentId());
        entity.setEmail(dto.getEmail());
        entity.setMessage(dto.getMessage());
        entity.setUser(userEntity);
        entity.setTreatment(treatmentEntity);
        return new ContactFormResponseDTO(contactFormRepository.save(entity));
    }

    /**
     * @param id 
     * @param contactFormRequestDTO
     * @return
     */
    @Override
    public ContactFormResponseDTO updateContactForm(Long id, ContactFormRequestDTO contactFormRequestDTO) {
        ContactFormEntity entity = contactFormRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No existe el formulario de contacto con ID: "+ id));
        if(contactFormRequestDTO.getEmail() != null) {
            entity.setEmail(contactFormRequestDTO.getEmail());
        }
        if(contactFormRequestDTO.getMessage() != null) {
            entity.setMessage(contactFormRequestDTO.getMessage());
        }
        return new ContactFormResponseDTO(contactFormRepository.save(entity));
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public ContactFormResponseDTO deleteContactForm(Long id) {
        ContactFormEntity entity = contactFormRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el formulario de contacto con ID: "+ id));
        contactFormRepository.delete(entity);
        return new ContactFormResponseDTO(entity);
    }


}
