<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-btn class="mx-2" fab dark color="indigo" v-bind="attrs" v-on="on">
                <v-icon dark>
                    mdi-plus
                </v-icon>
            </v-btn>
        </template>
        <v-card>
            <v-form ref="form">
                <v-card-title>
                    <span class="text-h5">Add Employee</span>
                </v-card-title>
                <v-card-text>
                    <v-container>
                        <v-row>
                          <!-- textfields organzied in columns  -->
                            <v-col cols="12">
                                <v-text-field v-model="name" label="Employee Name" :rules="[v => !!v]"
                                    required></v-text-field>
                            </v-col>

                            <v-col cols="12">
                                <v-text-field v-model="email" label="Email Address" :rules="[v => !!v]"
                                    required></v-text-field>
                            </v-col>

                            <v-col cols="12">
                                <v-text-field v-model="password" label="Password" type="password" autocomplete="on"
                                    :rules="[v => !!v]" required></v-text-field>
                            </v-col>
                        </v-row>
                    </v-container>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>

                  <!-- Buttons that close form or add changes -->
                    <v-btn color="blue darken-1" text @click="dialog = false">
                        Close
                    </v-btn>
                    <v-btn color="blue darken-1" text @click="createEmployee">
                        Add
                    </v-btn>
                </v-card-actions>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios'

export default {
    name: 'AddVisitorForm',

  //inputs for create employee
    data: () => ({
        dialog: false,
        name: '',
        email: '',
        password: '',
        valid: true
    }),

    methods: {
        createEmployee() {
            this.valid = this.$refs.form.validate();
            
            if (this.valid) {
                let options = {
                    method: 'POST',
                    url: `http://localhost:8090/employee`,
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    data: {
                        "name": this.name,
                        "emailAddress": this.email,
                        "password": this.password,
                        "museumId": sessionStorage.getItem("museum")
                    }
                };

                axios.request(options)
                    .then(response => response.data)
                    .then(() => {
                        this.$parent.$parent.$parent.getEmployees();
                        this.$refs.form.reset()
                        this.dialog = false;
                    })
                    .catch(err => console.error(err));
            }
        }
    },

}
</script>