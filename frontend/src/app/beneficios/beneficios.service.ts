import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BeneficioDTO } from './beneficio.dto';
import { TransferRequest } from './transfer-request';

@Injectable({
    providedIn: 'root'
})
export class BeneficiosService {
    private http = inject(HttpClient);
    private baseUrl = 'http://localhost:8080/api/v1/beneficios';

    list(): Observable<BeneficioDTO[]> {
        return this.http.get<BeneficioDTO[]>(this.baseUrl);
    }

    getById(id: number): Observable<BeneficioDTO> {
        return this.http.get<BeneficioDTO>(`${this.baseUrl}/${id}`);
    }

    create(beneficio: BeneficioDTO): Observable<BeneficioDTO> {
        return this.http.post<BeneficioDTO>(this.baseUrl, beneficio);
    }

    update(id: number, beneficio: BeneficioDTO): Observable<BeneficioDTO> {
        return this.http.put<BeneficioDTO>(`${this.baseUrl}/${id}`, beneficio);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    transfer(request: TransferRequest): Observable<any> {
        return this.http.post(`${this.baseUrl}/transfer`, request);
    }
}