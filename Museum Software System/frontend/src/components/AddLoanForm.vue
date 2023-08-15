<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark v-bind="attrs" v-on="on">
                New Loan Request
            </v-btn>
        </template>
        <v-card>
            <v-card-title>
                <span class="text-h5">New Loan Request</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-row>
                        <v-col cols="12" sm="6" md="3">
                            <v-text-field v-model="artworkId" label="Artwork ID" type="number" prefix="$"
                                required></v-text-field>
                        </v-col>
                    </v-row>
                </v-container>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
              <!-- buttons to close or submit loan form -->
                <v-btn color="blue darken-1" text @click="dialog = false">
                    Close
                </v-btn>
                <v-btn color="blue darken-1" text @click="createLoan()">
                    Submit
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios'

export default {
    name: 'AddLoanForm',

  //input data
    data: () => ({
        dialog: false,
        newLoan: '',
        errorLoan: '',
        artworkId: ''
    }),

    methods: {
        async createLoan() {
            var data = {
                "loaneeEmailAddress": sessionStorage.getItem("email"),
                "artworkId": this.artworkId,
                "museumId": sessionStorage.getItem("museum")
            };

            let options = {
                method: 'POST',
                url: 'http://localhost:8090/loan',
                headers: {
                    'Content-Type': 'application/json',
                },
                data: data
            };

            //checks of loans are created
            await axios.request(options)
                .then(response => {
                    this.newLoan = response.data
                    this.errorLoan = ''

                    if (this.newLoan == '')
                        alert("Artwork was already on loan; succesfully added to waitlist")
                    else alert("Loan request successfully submitted")
                })
                .catch(e => {
                    var errorMsg = e.response.data
                    console.log(errorMsg)
                    this.errorDonation = errorMsg
                    alert("Oops! An error has occurred: " + errorMsg)
                })

            this.$parent.$parent.$parent.$parent.getAllLoans()
            this.dialog = false
        }
    }

}
</script>