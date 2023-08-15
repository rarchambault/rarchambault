<!--This is the main login page where both the admin and visitor log into their respective portals. -->
<template>
   <v-app>
      <v-main>
         <v-container fluid fill-height>
            <v-layout align-center justify-center>
               <v-flex xs12 sm8 md4>
                  <v-card class="elevation-12">
                     <v-toolbar dark color="primary">
                        <v-toolbar-title>Login</v-toolbar-title>
                     </v-toolbar>
                     <v-card-text>

                        <v-form ref="form">
                              <!-- Text field to get email -->
                           <v-text-field v-model="email" label="Email" type="text" placeholder="email"
                              :rules="[v => !!v]" required></v-text-field>

                              <!-- Text field to get password -->
                           <v-text-field v-model="password" label="Password" type="password" :rules="[v => !!v]"
                              placeholder="password" autocomplete="on" required></v-text-field>

                           <div class="red--text"> {{ errorMessage }}</div>

                           <v-btn @click="loginAsVisitor" color="primary" value="log in" class="mt-4"> Login
                              as visitor</v-btn>

                              <!-- Button to login as admin -->
                           <v-btn @click="loginAsAdmin" class="ms-4 mt-4" color="primary"> Login
                              as admin
                           </v-btn>
                           <!-- Button to continue as guest -->
                           <v-btn color="green" class="mt-4" href="/visitor">
                              Continue as a guest
                           </v-btn>
                           <!-- Button to register as a visitor-->
                           <v-btn color="grey" class="mt-4" href="/signup">
                              Don't have an account? Register now!
                           </v-btn>
                        </v-form>
                     </v-card-text>
                  </v-card>
               </v-flex>
            </v-layout>
         </v-container>
      </v-main>
   </v-app>
</template>
 
<script>
//necessary imports
import axios from "axios";

export default {
   name: "LoginPage",

   data: () => {

      return {
         newVisitor: '',
         errorVisitor: '',
         email: '',
         password: '',
         dialog: false,
         confirmPassword: '',
         errorMessage: '',
         museumId: '',
         valid: true
      };
   },


   methods: {
      async loginAsVisitor() {
         this.valid = this.$refs.form.validate();
         //checks for valid inputs

         if (this.valid) {
            let options = {
               method: 'GET',
               url: `http://localhost:8090/visitor/emailAddress/${this.email}`,
               headers: {
                  'Content-Type': 'application/json',
               }
            };
            
            // Sets session parameters

            await axios.request(options)
               .then(response => response.data)
               .then((res) => {
                  if (res === "Visitor not found." || res.password !== this.password) {
                     this.errorMessage = "Incorrect email or password. Please try again."
                  } else {
                     sessionStorage.setItem("email", this.email);
                     sessionStorage.setItem("isOwner", false);
                     sessionStorage.setItem("isEmployee", false);
                     sessionStorage.setItem("isLoggedIn", true);
                     sessionStorage.setItem("museum", res.museum.id);
                     window.location = "/visitor";
                  }
               })
               .catch(err => console.error(err));
         }
      },

      //Get's museum id
      async getMuseumId(){
         let options = {
          method: 'GET',
          url: `http://localhost:8090/museum/name/Marwan's Fun House`,
          headers: {
            'Content-Type': 'application/json',
          }
        };

        return await axios.request(options)
          .then(response => response.data)
          .then((res) => {
            sessionStorage.setItem("museum", res.id);
            return res.id;
          })
          .catch(err => console.log(err));
      },

      async loginAsAdmin() {
         this.valid = this.$refs.form.validate();

         if (this.valid) {
            if (this.email == "marwan.kanaan@mcgill.ca" && this.password == "museum123") {
               sessionStorage.setItem("email", this.email);
               sessionStorage.setItem("isOwner", true);
               sessionStorage.setItem("isEmployee", true);
               sessionStorage.setItem("isLoggedIn", true);
               sessionStorage.setItem("museum", await this.getMuseumId());
               window.location = "/admin";
            } else {
               let options = {
                  method: 'GET',
                  url: `http://localhost:8090/employee/emailAddress/${this.email}`,
                  headers: {
                     'Content-Type': 'application/json',
                  }
               };

               await axios.request(options)
                  .then(response => response.data)
                  .then((res) => {
                     if (res === "Employee not found." || res.password !== this.password) {
                        this.errorMessage = "Incorrect email or password. Please try again."
                     } else {
                        sessionStorage.setItem("email", this.email);
                        sessionStorage.setItem("isOwner", false);
                        sessionStorage.setItem("isEmployee", true);
                        sessionStorage.setItem("isLoggedIn", true);
                        sessionStorage.setItem("museum", res.museum.id);
                        window.location = "/admin";
                     }
                  })
                  .catch(err => console.error(err));
            }
         }
      },
   }
}

</script>

