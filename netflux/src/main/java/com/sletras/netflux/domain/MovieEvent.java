package com.sletras.netflux.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author sergioletras
 */
@Data
@AllArgsConstructor
public class MovieEvent {

    private String movieId;
    private LocalDateTime movieDate;
}
