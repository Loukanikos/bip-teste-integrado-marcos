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
    <div class="container">
      <div class="header-actions">
        <h2>Benefícios</h2>
        <button class="btn btn-primary" (click)="novo()">
          <i class="fas fa-plus"></i> Novo Benefício
        </button>
      </div>

      <div class="glass-card">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Valor</th>
              <th style="text-align: right">Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let b of beneficios">
              <td>#{{ b.id }}</td>
              <td><strong>{{ b.nome }}</strong></td>
              <td>R$ {{ b.valor | number:'1.2-2' }}</td>
              <td style="text-align: right">
                <div style="display: flex; gap: 0.5rem; justify-content: flex-end;">
                  <button class="btn btn-outline btn-icon" (click)="transferir(b)" title="Transferir">
                    <i class="fas fa-exchange-alt"></i>
                  </button>
                  <button class="btn btn-outline btn-icon" (click)="editar(b.id)" title="Editar">
                    <i class="fas fa-edit"></i>
                  </button>
                  <button class="btn btn-danger btn-icon" (click)="excluir(b.id)" title="Excluir">
                    <i class="fas fa-trash"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        
        <div *ngIf="beneficios.length === 0" style="text-align: center; padding: 2rem; color: var(--text-secondary);">
          Nenhum benefício encontrado.
        </div>
      </div>
    </div>

    <style>
      .container { max-width: 1000px; margin: 0 auto; }
      .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; }
    </style>
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
    if (confirm('Deseja realmente excluir este benefício?')) {
      this.beneficiosService.delete(id).subscribe(() => this.carregar());
    }
  }

  transferir(beneficio: BeneficioDTO) {
    this.router.navigate(['/beneficios/transferir', beneficio.id]);
  }
}