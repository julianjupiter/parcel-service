package xyz.mynt.parcel.dto;

import java.net.URI;
import java.time.Instant;

/**
 * @author Julian Jupiter
 */
public record DataResponseDto(String title, int status, Object data, URI path, Instant createdAt) {}
