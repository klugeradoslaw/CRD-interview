import {useNavigate, useSearchParams} from 'react-router';
import {Alert, Box, Button, CircularProgress, Grid, Typography,} from "@mui/material";
import RentalDatesForm from "../components/RentalDatesForm.tsx";
import useGetCars from "../api/useGetCars.ts";
import CarCard from "../components/CarCard.tsx";

const AvailableCars = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const startParam = searchParams.get('startDate');
    const endParam = searchParams.get('endDate');

    const {data: cars = [], isLoading: loading, error} = useGetCars(startParam, endParam);

    return (
        <Box sx={{minHeight: '100vh', backgroundColor: '#121212', p: {xs: 2, md: 4}, color: '#fff'}}>
            <Box sx={{maxWidth: '1200px', margin: '0 auto'}}>
                <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3}}>
                    <Typography variant="h4">
                        Available cars
                    </Typography>
                    <Button variant="outlined" color="inherit" onClick={() => navigate('/')}>
                        Home Page
                    </Button>
                </Box>

                <RentalDatesForm direction="row" submitButtonText="Update search"/>

                {loading && (
                    <Box sx={{display: 'flex', justifyContent: 'center', my: 8}}>
                        <CircularProgress/>
                    </Box>
                )}

                {error && (
                    <Alert severity="error" sx={{mb: 3}}>
                        {error.message}
                    </Alert>
                )}

                {!loading && !error && cars.length === 0 && (
                    <Typography sx={{mt: 6, textAlign: 'center', color: '#888'}}>
                        No cars available for the selected dates.
                    </Typography>
                )}

                <Grid sx={{display: 'flex', flexDirection: 'column', gap: 2, mt: 4}}>
                    {cars.map((car) => (
                        <CarCard key={car.id} car={car}/>
                    ))}
                </Grid>
            </Box>
        </Box>
    );
};

export default AvailableCars;