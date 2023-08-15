<template>
    <div>
        <v-card variant="tonal">
            <v-list-item three-line>
                <v-list-item-content>
                    <v-card-title>Employees Overview</v-card-title>
                </v-list-item-content>
                <AddEmployeeForm />
            </v-list-item>

            <v-simple-table width="100px">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email Address</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="employee in employees" :key="employee.emailAddress">
                        <td>{{ employee.name }}</td>
                        <td>{{ employee.emailAddress }}</td>
                        <td>
                            <v-btn class="ma-2" color="error" @click="deleteEmployee(employee.emailAddress)">
                                🔥Fire🔥
                                <v-icon end icon="mdi-checkbox-marked-circle"></v-icon>
                            </v-btn>
                        </td>
                        <td>
                            <AddEmployeeShiftForm :email="employee.emailAddress"/>
                        </td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-card>

        <br />

        <v-card variant="tonal">
            <v-list-item three-line>
                <v-list-item-content>
                    <v-card-title>All Shifts</v-card-title>
                </v-list-item-content>
            </v-list-item>

            <v-simple-table width="100px">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Employee</th>
                        <th>Date</th>
                        <th>Start Time</th>
                        <th>End Time</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="event in events" :key="event.id">
                        <td>{{ event.id }}</td>
                        <td>{{ event.employee }}</td>
                        <td>{{ event.date }}</td>
                        <td>{{ event.start }}</td>
                        <td>{{ event.end }}</td>
                        <td>
                            <v-btn class="ma-2" color="error" @click="deleteShift(event.id)">
                                Delete
                                <v-icon end icon="mdi-checkbox-marked-circle"></v-icon>
                            </v-btn>
                        </td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-card>
    </div>
</template>

<script>

import axios from 'axios';
import AddEmployeeForm from './AddEmployeeForm.vue';
import AddEmployeeShiftForm from './AddEmployeeShiftForm.vue';

export default {
    name: 'EmployeesOverview',

    components: { AddEmployeeForm, AddEmployeeShiftForm },

    data() {
        return {
            employees: [],
            events: []
        }
    },

    methods: {
        getEmployees() {
            let options = {
                method: 'GET',
                url: `http://localhost:8090/employee`,
                headers: {
                    'Content-Type': 'application/json',
                },
            };

            axios.request(options)
                .then(response => response.data)
                .then(employees => {
                    this.employees = employees;
                    this.getShifts();
                })
                .catch(err => console.error(err));
        },

        async deleteEmployee(email) {
            let shiftOption = {
                method: 'GET',
                url: `http://localhost:8090/shift/employeeEmailAddress/${email}`,
                headers: {
                    'Content-Type': 'application/json',
                }
            }

            const employeeShifts = await axios.request(shiftOption)
                .then(response => response.data)
                .then((res) => {
                    if (res === "Shifts not found.") return [];
                    return res;
                })
                .catch(err => console.error(err));

            await Promise.all(employeeShifts.map(async employeeShift => {
                let shiftOptionDelete = {
                    method: 'DELETE',
                    url: `http://localhost:8090/shift/id/${employeeShift.id}`,
                    headers: {
                        'Content-Type': 'application/json',
                    }
                }

                await axios.request(shiftOptionDelete)
                    .catch(err => console.log(err));
            }))
            .then(() => {
                let options = {
                    method: 'DELETE',
                    url: `http://localhost:8090/employee/emailAddress/${email}`,
                    headers: {
                        'Content-Type': 'application/json',
                    }
                };

                axios.request(options)
                    .then(response => response.data)
                    .then(() => {
                        this.getEmployees();
                    })
                    .catch(err => console.error(err));                
            })
    },

    getShifts() {
        this.events = [];

        this.employees.forEach(employee => {
            let options = {
                method: 'GET',
                url: `http://localhost:8090/shift/employeeEmailAddress/${employee.emailAddress}`,
                headers: {
                    'Content-Type': 'application/json',
                }
            };

            axios.request(options)
                .then(response => response.data)
                .then((shiftRes) => {
                    if (shiftRes === 'Shifts not found.') return;
                    shiftRes.forEach(shift => {
                        this.events.push({
                            id: shift.id,
                            employee: employee.name,
                            date: shift.day.date,
                            start: shift.startTime,
                            end: shift.endTime
                        });
                    });
                })
        });
    },

    deleteShift(id) {
        let options = {
            method: 'DELETE',
            url: `http://localhost:8090/shift/id/${id}`,
            headers: {
                'Content-Type': 'application/json',
            }
        };

        axios.request(options)
            .catch(err => console.error(err));

        this.events = this.events.filter(shift => {
            if (shift.id !== id) return shift;
        })
    }
},

created() {
    this.getEmployees();
}
}
</script>