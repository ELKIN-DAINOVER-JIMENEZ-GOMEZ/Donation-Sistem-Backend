package barrioFunde.demo.application.ports.in;

import barrioFunde.demo.domain.model.Donacion;

public interface CrearDonacionUseCase {
    Donacion crear(Donacion donacion);
}