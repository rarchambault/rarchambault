<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark v-bind="attrs" v-on="on">
                New Donation Request
            </v-btn>
        </template>
        <v-card>
          <!-- Title of popup form  -->
            <v-card-title>
                <span class="text-h5">New Donation Request</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-row>
                      <!-- Labels for each column and v-model is what is written in textfield  -->
                        <v-col cols="12">
                            <v-text-field v-model="nameField" label="Donation Name" required></v-text-field>
                        </v-col>
                    </v-row>
                </v-container>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>

              <!-- Close and submit buttons that close or submit the info in the form  -->
                <v-btn color="blue darken-1" text @click="dialog = false">
                    Close
                </v-btn>
                <v-btn color="blue darken-1" text @click="createDonation()">
                    Submit
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios'

export default {
    name: 'AddDonationForm',

    data: () => ({
        dialog: false,
        newDonation: '',
        errorDonation: '',
        nameField: ''
    }),

    methods: {
        async createDonation() {

            if (sessionStorage.getItem("email") == "")
            {
                alert("Error: You need to login to submit a new donation request.")
                window.location = "/login"
                return
            }
            // input perameters
            var data = {
                "donatorEmailAddress": sessionStorage.getItem("email"),
                "name": this.nameField,
                "museumId": sessionStorage.getItem("museum")
            };

            let options = {
                method: 'POST',
                url: 'http://localhost:8090/donation',
                headers: {
                    'Content-Type': 'application/json',
                },
                data: data
            };
            // sses if donation was created properly
            await axios.request(options)
                .then(response => {
                    this.newDonation = response.data
                    this.errorDonation = ''
                    alert("Donation request successfully submitted")
                })
                .catch(e => {
                    var errorMsg = e.response.data.message
                    console.log(errorMsg)
                    this.errorDonation = errorMsg
                    alert("Oops! An error has occurred: " + errorMsg)
                })

            this.$parent.$parent.$parent.$parent.getAllDonations()
            this.dialog = false
        }
    }

}
</script>