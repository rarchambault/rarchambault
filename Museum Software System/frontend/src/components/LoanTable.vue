<!-- Table Component for Loans-->
<template>
    <v-app align="center">
        <v-card>
            <v-list-item three-line>
                <v-list-item-content>
                    <v-card-title>My Loans</v-card-title>
                </v-list-item-content>
                <AddLoanForm />
            </v-list-item>
            <v-simple-table>
                <thead>
                    <tr> <!-- Table Parameters -->
                        <th class="text-left">
                            ID
                        </th>
                        <th class="text-left">
                            Artwork ID
                        </th>
                        <th class="text-left">
                            Artwork Name
                        </th>
                        <th class="text-left">
                            Start Date
                        </th>
                        <th class="text-left">
                            End Date
                        </th>
                        <th class="text-left">
                            Number of Renewals
                        </th>
                        <th class="text-left">
                            Status
                        </th>
                        <th class="text-left">
                            Late?
                        </th>
                        <th class="text-left">
                            Late Fee
                        </th>
                        <th class="text-left">
                            Fee Paid?
                        </th>
                        <th class="text-left">
                            Pay Fee
                        </th>
                        <th class="text-left">
                            Renew
                        </th>
                        <th class="text-left">
                            Return
                        </th>
                    </tr>
                </thead>
                <tbody> <!-- Table Body with Parameters -->
                    <tr v-for="loan in loans" :key="loan.id">
                        <td>{{ loan.id }}</td>
                        <td>{{ loan.artwork.id }}</td>
                        <td>{{ loan.artwork.name }}</td>
                        <td>{{ loan.startDate }}</td>
                        <td>{{ loan.endDate }}</td>
                        <td>{{ loan.numberOfRenewals }}</td>
                        <td>{{ loan.status }}</td>
                        <td>{{ loan.isLate === true ? "Yes" : "No" }}</td>
                        <td>{{ loan.lateFee }}</td>
                        <td>{{ loan.isFeePaid === true ? "Yes" : "No" }}</td>
                        <td>
                            <v-btn v-if="(!loan.isFeePaid && loan.status !== 'Rejected')" color="green" class="mt-0"
                                @click="payLoan(loan.id, loan.artwork.loanFee)"> Pay Fee </v-btn>
                        </td>
                        <td>
                            <v-btn v-if="loan.isFeePaid && loan.status !== 'Returned'" color="green" class="mt-0" @click="renewLoan(loan.id, loan.artwork.loanFee)"> Renew
                            </v-btn>
                        </td>
                        <td>
                            <v-btn v-if="loan.isFeePaid && loan.status !== 'Returned'" color="green" class="mt-0" @click="returnLoan(loan.id, loan.lateFee)">
                                Return </v-btn>
                        </td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-card>
    </v-app>
</template>

<script>
//imports
import axios from 'axios'
import AddLoanForm from './AddLoanForm.vue'

var backendUrl = 'http://localhost:8090'

// Instantiate axios client
var AXIOS = axios.create({
    baseURL: backendUrl,
})

export default {
    name: 'LoanTable',

    components: { AddLoanForm },

    data() {
        return {
            loans: [],
        };
    },

    methods: {
        // Gets all loans associated with the current visitor
        getAllLoans() {
            if (sessionStorage.getItem("email") == "") {
                alert("Error: You need to login to view and submit loan requests.")
                window.location = "/login"
                return
            }

            AXIOS
                .get('/loan/loaneeEmailAddress/' + sessionStorage.getItem("email"))
                .then(res => res.data)
                .then(response => {
                    console.log(response)
                    this.loans = response;
                })
                .catch(error => {
                    console.log(error)
                    alert("Oops! An error occurred " + error)
                    window.location = "/login";
                })
        },
        // Method to Pay Loan 
        async payLoan(loanId, inputAmount) {
            var data = {
                "loanId": loanId,
                "inputAmount": inputAmount,
            };

            let options = {
                method: 'POST',
                url: 'http://localhost:8090/loan/pay',
                headers: {
                    'Content-Type': 'application/json',
                },
                data: data
            };

            await axios.request(options)
                .then(
                    alert("Successfully paid for loan")
                )
                .catch(e => {
                    var errorMsg = e.response.data
                    console.log(errorMsg)
                    this.errorDonation = errorMsg
                    alert("Oops! An error has occurred: " + errorMsg)
                });

                this.getAllLoans()
        },

        //Renew Loan method
        async renewLoan(loanId, inputAmount) {
            var data = {
                "loanId": loanId,
                "inputAmount": inputAmount,
            };

            let options = {
                method: 'POST',
                url: 'http://localhost:8090/loan/renew',
                headers: {
                    'Content-Type': 'application/json',
                },
                data: data
            };

           await axios.request(options)
                .then(
                    alert("Successfully renewed loan for two weeks")
                )
                .catch(e => {
                    var errorMsg = e.response.data
                    console.log(errorMsg)
                    this.errorDonation = errorMsg
                    alert("Oops! An error has occurred: " + errorMsg)
                });

                this.getAllLoans()
        },

        //return loan method
        async returnLoan(loanId, inputAmount) {
            var data = {
                "loanId": loanId,
                "inputAmount": inputAmount,
            };

            let options = {
                method: 'POST',
                url: 'http://localhost:8090/loan/return',
                headers: {
                    'Content-Type': 'application/json',
                },
                data: data
            };

            await axios.request(options)
                .then(
                    alert("Successfully returned loan")
                )
                .catch(e => {
                    var errorMsg = e.response.data
                    console.log(errorMsg)
                    this.errorDonation = errorMsg
                    alert("Oops! An error has occurred: " + errorMsg)
                });

                this.getAllLoans()
        }
    },


    // Get all loans when the page is loaded
    created() {
        this.getAllLoans()
    }
}

</script>