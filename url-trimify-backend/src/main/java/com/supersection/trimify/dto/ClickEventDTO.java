package com.supersection.trimify.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClickEventDTO {
  private LocalDate clickDate;
  private Long count;
}
