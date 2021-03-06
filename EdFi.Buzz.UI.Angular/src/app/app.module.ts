import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ApolloModule, APOLLO_OPTIONS } from 'apollo-angular';
import { HttpLinkModule, HttpLink } from 'apollo-angular-link-http';
import { InMemoryCache } from 'apollo-cache-inmemory';

import { AppComponent } from './app.component';
import { HomeComponent } from './Features/home/home.component';
import { StudentCardComponent } from './Components/StudentCard/studentCard.component';
import { TeacherLandingComponent } from './Features/Landings/TeacherLanding/teacherLanding.component';
import { NavbarComponent } from './Features/navbar/navbar.component';
import { hasLifecycleHook } from '@angular/compiler/src/lifecycle_reflector';
import { GuardianCardComponent } from './Components/GuardianCard/guardianCard.component';
import { StudentCardLiteComponent } from './Components/StudentCardLite/studentCardLite.component';
import { SiblingCardComponent } from './Components/SiblingCard/siblingCard.component';
import { SurveyCardComponent } from './Components/SurveyCard/surveyCard.component';
import { StudentDetailComponent } from './Features/StudentDetail/studentDetail.component';
import { AuthServiceConfig, GoogleLoginProvider, SocialLoginModule } from 'angularx-social-login';
import { LoginComponent } from './Features/Login/login.component';
import { ChartsModule } from 'ng2-charts';
import { SurveyAnalytics2Component } from './Features/SurveyAnalytics2/surveyAnalytics2.component';
import { TooltipModule } from 'ng2-tooltip-directive';
import { JwtInterceptor } from './Interceptors/jwt.interceptor';
import { AuthGuard } from './Interceptors/auth.guard';

const config = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider('761615059487-5tuhthkic53s5m0e40k6n68hrc7i3udp.apps.googleusercontent.com')
  }
]);

export function provideConfig() {
  return config;
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    TeacherLandingComponent,
    NavbarComponent,
    StudentCardComponent,
    StudentCardLiteComponent,
    GuardianCardComponent,
    SiblingCardComponent,
    SurveyCardComponent,
    StudentDetailComponent,
    SurveyAnalytics2Component,
    LoginComponent
  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'ng-cli-universal' }),
    BrowserAnimationsModule,
    HttpClientModule,
    SocialLoginModule,
    ApolloModule,
    HttpLinkModule,
    FormsModule,
    ChartsModule,
    TooltipModule,
    RouterModule.forRoot([
      {
        path: 'app', component: HomeComponent, children: [ // this displays the navbar
          { path: '', component: TeacherLandingComponent },
          { path: 'studentDetail/:id', component: StudentDetailComponent },
          { path: 'surveyAnalytics2', component: SurveyAnalytics2Component },
        ],
        canActivate: [AuthGuard],
      },
      { path: 'login', component: LoginComponent },
      // when security and auth guards applied change it to redirect to '' then auth guard login will redirect to login if necessary.
      { path: '**', redirectTo: 'app' }
    ], { useHash: true, scrollPositionRestoration: 'enabled' })
  ],
  providers: [
    { provide: AuthServiceConfig, useFactory: provideConfig },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    {
      provide: APOLLO_OPTIONS,
      useFactory: (httpLink: HttpLink) => {
        return {
          cache: new InMemoryCache(),
          link: httpLink.create({
            uri: 'http://localhost:3000/graphql'
          })
        };
      },
      deps: [HttpLink]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
