<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark v-bind="attrs" v-on="on">
                Purchase Museum Pass
            </v-btn>
        </template>
        <v-card>
            <v-card-title>
                <span class="text-h5">Purchase Museum Pass</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-row>
                        <v-label>Visit Date</v-label>
                      <!-- calender to pic dates-->
                        <v-date-picker v-model="datePicker" style="display: block; margin: auto; width: fit-content;">
                        </v-date-picker>
                        <v-col cols="12">
                            <v-select v-model="type" :items="types" label="Pass Type"></v-select>
                        </v-col>
                    </v-row>
                </v-container>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
              <!-- button to close or submit values in form -->
                <v-btn color="blue darken-1" text @click="dialog = false">
                    Close
                </v-btn>
                <v-btn color="blue darken-1" text @click="createMuseumPass()">
                    Submit
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios'

export default {
    name: 'AddMuseumPassForm',


    // input parameters
    data: () => ({
        dialog: false,
        newMuseumPass: '',
        errorMuseumPass: '',
        types: ["Regular", "Senior", "Student", "Child"],
        type: '',
        datePicker: ''
    }),

    methods: {
        async createMuseumPass() {
            var data = {
                "visitorEmailAddress": sessionStorage.getItem("email"),
                "date": this.datePicker,
                "museumId": sessionStorage.getItem("museum"),
                "type": this.type
            };

            let options = {
                method: 'POST',
                url: 'http://localhost:8090/museumPass',
                headers: {
                    'Content-Type': 'application/json',
                },
                data: data
            };
            //checks if Museum pass is created
            await axios.request(options)
                .then(response => {
                    this.newMuseumPass = response.data
                    this.errorMuseumPass = ''
                    alert("Museum pass successfully purchased")
                })
                .catch(e => {
                    var errorMsg = e.response.data
                    console.log(errorMsg)
                    this.errorDonation = errorMsg
                    alert("Oops! An error has occurred: " + errorMsg)
                });

            this.$parent.$parent.$parent.$parent.getAllPasses()
            this.dialog = false
        }
    }

}
</script>