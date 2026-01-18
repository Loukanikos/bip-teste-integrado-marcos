import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { BENEFICIOS_ROUTES } from './app/beneficios/beneficios.routes';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    provideRouter([
      { path: 'beneficios', children: BENEFICIOS_ROUTES },
      { path: '', redirectTo: 'beneficios', pathMatch: 'full' }
    ])
  ]
}).catch(err => console.error(err));