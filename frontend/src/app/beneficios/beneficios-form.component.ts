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
  templateUrl: './beneficios-form.component.html',
  styles: [`
      .container { max-width: 600px; margin: 0 auto; }
      .form-group { margin-bottom: 1.5rem; }
      .form-actions { display: flex; gap: 1rem; margin-top: 2rem; }
    `]
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