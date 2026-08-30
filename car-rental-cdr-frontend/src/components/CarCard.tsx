import type {Car} from "../api/useGetCars.ts";
import Card from "./Card.tsx";
import {Box, Button, CircularProgress, Typography} from "@mui/material";
import usePostReservation from "../api/usePostReservation.ts";
import {useSearchParams} from "react-router";
import type {ReactNode} from "react";

interface CarCardProps {
    car: Car;
}

const CarCard = ({car}: CarCardProps) => {
    const [searchParams] = useSearchParams();

    const startParam = searchParams.get('startDate');
    const endParam = searchParams.get('endDate');
    const {mutate: postReservation, isPending} = usePostReservation();

    const handleReserve = (carId: number | string) => {
        if (!startParam || !endParam) return;
        postReservation(
            {carId, startDateTime: startParam, endDateTime: endParam},
            {
                onSuccess: (data) => {
                    alert(`Pomyślnie zarezerwowano samochód ${data.car.brand} ${data.car.model}!`)
                },
                onError: (err) => {
                    alert(err.message);
                },
            }
        );
    };

    return (
        <Card
            sx={{
                p: 2.5,
                display: 'flex',
                flexDirection: {xs: 'column', md: 'row'},
                alignItems: 'center',
                justifyContent: 'space-between',
                gap: 2,
                width: '100%',
            }}
        >
            <CarInfoItem label="Type" value={car.type}/>
            <CarInfoItem label="Brand" value={car.brand}/>
            <CarInfoItem label="Model" value={car.model}/>
            <Box sx={{flex: 1, width: '100%', display: 'flex', justifyContent: {xs: 'stretch', md: 'flex-end'}}}>
                <Button
                    variant="contained"
                    color="primary"
                    size="large"
                    fullWidth
                    disabled={isPending}
                    onClick={() => handleReserve(car.id)}
                    sx={{height: '48px', fontWeight: 600, maxWidth: {md: '200px'}}}
                >
                    {isPending ? <CircularProgress size={24} color="primary"/> : "Book this car"}
                </Button>
            </Box>
        </Card>
    );
};

interface CarInfoItemProps {
    label: string;
    value: ReactNode
}

const CarInfoItem = ({label, value}: CarInfoItemProps) => {
    return (
        <Box sx={{flex: 1, width: "100%"}}>
            <Typography
                variant="caption"
                sx={{color: "#888", textTransform: "uppercase", display: "block"}}
            >
                {label}
            </Typography>
            <Typography variant="h6">
                {value}
            </Typography>
        </Box>
    );
};

export default CarCard;