import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BeneficiosService } from './beneficios.service';
import { BeneficioDTO } from './beneficio.dto';

@Component({
    selector: 'app-beneficios-form',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    template: `
    <h2>{{ beneficio.id ? 'Editar' : 'Novo' }} Benefício</h2>
    <form (ngSubmit)="salvar()">
      <label>Nome:</label>
      <input [(ngModel)]="beneficio.nome" name="nome" required />

      <label>Valor:</label>
      <input type="number" [(ngModel)]="beneficio.valor" name="valor" required />

      <button type="submit">Salvar</button>
      <button type="button" (click)="voltar()">Cancelar</button>
    </form>
  `
})
export class BeneficiosFormComponent implements OnInit {
    beneficio: BeneficioDTO = { nome: '', valor: 0 };

    constructor(
        private beneficiosService: BeneficiosService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit() {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.beneficiosService.getById(+id).subscribe(data => this.beneficio = data);
        }
    }

    salvar() {
        if (this.beneficio.id) {
            this.beneficiosService.update(this.beneficio.id, this.beneficio).subscribe(() => this.voltar());
        } else {
            this.beneficiosService.create(this.beneficio).subscribe(() => this.voltar());
        }
    }

    voltar() {
        this.router.navigate(['/beneficios']);
    }
}