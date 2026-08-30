import {type ReactNode} from 'react';
import {Box, type SxProps} from "@mui/material";

interface CardProps {
    children: ReactNode;
    sx?: SxProps;
}

const Card = ({ children, sx }: CardProps) => {
    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: {xs: 'column', md: 'row'},
                border: '1px solid #333333',
                borderRadius: 4,
                backgroundColor: '#1e1e1e',
                boxShadow: '0px 10px 30px rgba(0, 0, 0, 0.6)',
                ...sx,
            }}
        >
            {children}
        </Box>
    );
};

export default Card;