package com.god.MediaManager.DTO;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {
    private int page = 0;
    private int itemPerPage = 10;
    private String sortDirection = "ASC";

    public Sort.Direction getDirection() {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        return direction;
    }

    public static PageRequest getPageRequest(@Nullable PaginationRequest paginationRequest, String properties) {
        if(paginationRequest==null){
            paginationRequest = new PaginationRequest();
        }
        if(properties.isEmpty()){
            return  PageRequest.of(
                    paginationRequest.getPage(),
                    paginationRequest.getItemPerPage()
            );
        }
        else{
            return PageRequest.of(
                    paginationRequest.getPage(),
                    paginationRequest.getItemPerPage(),
                    paginationRequest.getDirection(),
                    properties);
        }
    }
}
