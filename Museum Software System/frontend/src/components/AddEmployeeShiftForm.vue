<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-card-title>
                <v-btn class="ma-2" color="success" v-bind="attrs" v-on="on">
                    Schedule
                    <v-icon end icon="mdi-checkbox-marked-circle"></v-icon>
                </v-btn>
            </v-card-title>
        </template>
        <v-card>
            <v-form ref="form">
                <v-card-title>
                    <span class="text-h5">Add a Shift</span>
                </v-card-title>
                <v-card-text>
                    <br />
                    <v-date-picker v-model="picker" style="display: block; margin: auto; width: fit-content;">
                    </v-date-picker>
                    <v-row>
                        <v-col cols="12" sm="6">
                            <v-dialog ref="dialog1" v-model="modal1" :return-value.sync="startTime" persistent
                                width="290px">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-text-field v-model="startTime" label="Start Time"
                                        prepend-icon="mdi-clock-time-four-outline" readonly v-bind="attrs" v-on="on">
                                    </v-text-field>
                                </template>
                                <v-time-picker v-if="modal1" v-model="startTime" full-width>
                                    <v-spacer></v-spacer>
                                    <v-btn text color="primary" @click="modal1 = false">
                                        Cancel
                                    </v-btn>
                                    <v-btn text color="primary" @click="$refs.dialog1.save(startTime)">
                                        OK
                                    </v-btn>
                                </v-time-picker>
                            </v-dialog>
                        </v-col>
                        <v-col cols="12" sm="6">
                            <v-dialog ref="dialog2" v-model="modal2" :return-value.sync="endTime" persistent
                                width="290px">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-text-field v-model="endTime" label="End Time"
                                        prepend-icon="mdi-clock-time-four-outline" readonly v-bind="attrs"
                                        v-on="on"></v-text-field>
                                </template>
                                <v-time-picker v-if="modal2" v-model="endTime" full-width>
                                    <v-spacer></v-spacer>
                                    <v-btn text color="primary" @click="modal2 = false">
                                        Cancel
                                    </v-btn>
                                    <v-btn text color="primary" @click="$refs.dialog2.save(endTime)">
                                        OK
                                    </v-btn>
                                </v-time-picker>
                            </v-dialog>
                        </v-col>
                    </v-row>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="dialog = false">
                        Close
                    </v-btn>
                    <v-btn color="blue darken-1" text @click="addShift">
                        Add
                    </v-btn>
                </v-card-actions>
            </v-form>
        </v-card>
    </v-dialog>


</template>

<script>
import axios from 'axios';

const date = new Date();

export default {
    name: 'AddEmployeeShiftForm',

    props: {
        email: String
    },

    data: () => ({
        dialog: false,
        dialog1: false,
        dialog2: false,
        startTime: '08:00',
        endTime: '17:00',
        menu1: false,
        menu2: false,
        modal1: false,
        modal2: false,
        picker: date.toISOString().split('T')[0],
    }),

    methods: {
        addShift() {
            let options = {
                method: 'POST',
                url: `http://localhost:8090/day`,
                headers: {
                    'Content-Type': 'application/json',
                },
                data:
                {
                    "museumId": sessionStorage.getItem("museum"),
                    "openingHour": "8",
                    "closingHour": "17",
                    "date": this.picker,
                    "isHoliday": false
                }
            };

            console.log(options);

            let shiftOptions = {
                method: 'POST',
                url: `http://localhost:8090/shift`,
                headers: {
                    'Content-Type': 'application/json',
                },
                data: {
                    "dayDate": this.picker,
                    "startTime": this.startTime,
                    "endTime": this.endTime,
                    "employeeEmailAddress": this.email,
                    "museumId": sessionStorage.getItem("museum")
                }

            };

            axios.request(options)
                .then(response => response.data)
                .then(() => {
                    //console.log({ "day": res });
                    axios.request(shiftOptions)
                        .then(response => response.data)
                        .then(() => {
                            //console.log({ "shift": shiftRes });
                            this.$parent.$parent.$parent.getShifts();
                            this.dialog = false;
                        })
                })
                .catch(err => console.error(err));
        }
    }
}
</script>