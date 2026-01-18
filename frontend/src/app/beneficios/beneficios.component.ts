import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BeneficiosService } from './beneficios.service';
import { BeneficioDTO } from './beneficio.dto';
import { BeneficiosListComponent } from './beneficios-list.component';
import { BeneficiosFormComponent } from './beneficios-form.component';

@Component({
    selector: 'app-beneficios',
    standalone: true,
    imports: [CommonModule, BeneficiosListComponent, BeneficiosFormComponent],
    template: `
    <app-beneficios-list></app-beneficios-list>
    <app-beneficios-form></app-beneficios-form>
  `
})
export class BeneficiosComponent implements OnInit {
    beneficios: BeneficioDTO[] = [];
    private beneficiosService = new BeneficiosService();

    ngOnInit() {
        this.beneficiosService.list().subscribe(data => {
            this.beneficios = data;
        });
    }
}