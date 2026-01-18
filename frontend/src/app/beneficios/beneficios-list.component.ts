import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { BeneficiosService } from './beneficios.service';
import { BeneficioDTO } from './beneficio.dto';

@Component({
    selector: 'app-beneficios-list',
    standalone: true,
    imports: [CommonModule, RouterModule],
    template: `
    <h2>Lista de Benefícios</h2>
    <button (click)="novo()">Novo Benefício</button>
    <ul>
      <li *ngFor="let b of beneficios">
        {{ b.nome }} - R$ {{ b.valor }}
        <button (click)="editar(b.id)">Editar</button>
        <button (click)="excluir(b.id)">Excluir</button>
      </li>
    </ul>
  `
})
export class BeneficiosListComponent implements OnInit {
    beneficios: BeneficioDTO[] = [];

    constructor(private beneficiosService: BeneficiosService, private router: Router) { }

    ngOnInit() {
        this.carregar();
    }

    carregar() {
        this.beneficiosService.list().subscribe(data => this.beneficios = data);
    }

    novo() {
        this.router.navigate(['/beneficios/novo']);
    }

    editar(id?: number) {
        if (id) {
            this.router.navigate(['/beneficios/editar', id]);
        }
    }

    excluir(id?: number) {
        if (!id) return;
        this.beneficiosService.delete(id).subscribe(() => this.carregar());
    }
}